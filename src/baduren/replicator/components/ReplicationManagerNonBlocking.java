package baduren.replicator.components;
// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability.
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or
// data to be ensured and,  more generally, to use and operate it in the
// same conditions as regards security.
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.


import baduren.components.Broker.Broker;
import baduren.replicator.interfaces.CombinatorI;
import baduren.replicator.interfaces.PortFactoryI;
import baduren.replicator.interfaces.ReplicableCI;
import baduren.replicator.interfaces.SelectorI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.*;
import fr.sorbonne_u.components.ports.*;

import java.util.*;
import java.util.concurrent.*;

// -----------------------------------------------------------------------------

/**
 * The class <code>ReplicationManagerNonBlocking</code> introduces the
 * possibility to call many replicas but returning the first result while
 * letting the other (repeated) computations terminating (not possible with
 * <code>invokeAny</code>, an important behaviour when using replication for
 * fault-tolerance.
 *
 * <p><strong>Description</strong></p>
 *
 * <p>
 * The call mode <code>FIRST</code> and its implementation in the method
 * <code>call</code> shows how to perform this kind of waiting. Some care is
 * taken to partition the exceptions between the ones that result from a
 * malfunctioning server and the ones that result from a wrong computation.
 * The latter must be thrown to the caller but the former should trigger
 * fault-tolerance actions which are not implemented here.
 * </p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 *
 * <p>Created on : 2020-03-05</p>
 *
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
// -----------------------------------------------------------------------------
@OfferedInterfaces(offered = {ReplicableCI.class})
@RequiredInterfaces(required = {ReplicableCI.class})
// -----------------------------------------------------------------------------
public class			ReplicationManagerNonBlocking<T>
        extends		ReplicationManager<T>
{
    protected static final String	CALL_POOL_URI = "call-pool" ;

    /**
     * The enumeration <code>CallMode</code> defines the different call
     * semantics for the replicas by the replication manager.
     *
     * <p><strong>Description</strong></p>
     *
     *
     * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
     */
    public static enum	CallMode {
        /** only one (selected) server is called.							*/
        SINGLE,
        /** all selected servers are called, the result of the first to
         *  respond is returned and others are cancelled.					*/
        ANY,
        /** all selected servers are called, the result of the first to
         *  respond is returned but others can finish their execution.		*/
        FIRST,
        /** all selected servers are called and all of the results are
         *  collected.	*/
        ALL
    }

    /** asynchronous call mode of the replication manager.					*/
    protected CallMode	callMode ;
    /** semaphore used to ensure mutual exclusion on the computations when
     *  necessary															*/

    /**
     * creating an asynchronous replication manager.
     *
     * <p><strong>Description</strong></p>
     *
     * <p>
     * If the call mode is <code>ANY</code>, it is better to have
     * {@code nbThreads == serverInboundPortURIs.length} <i>i.e.</i>, the
     * number of threads is equal to the number of servers, so that all of
     * them have a chance to respond otherwise the "race" will be only among
     * the first bunch of servers called with the number of available threads.
     * If the call mode is <code>FIRST</code> (return the first result to come
     * in but let the other servers execute the request), the same may apply,
     * but not necessarily.
     * </p>
     * <p>
     * If the call mode is <code>SINGLE</code> or <code>ALL</code>, the number
     * of threads determines respectively the number of different requests or
     * copies of a request that can be executed in parallel.
     * </p>
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code nbThreads > 0}
     * pre	{@code ownInboundPortURI != null}
     * pre	{@code selector != null && ThreadSafe(selector)}
     * pre	{@code mode != null}
     * pre	{@code combinator != null && ThreadSafe(combinator)}
     * pre	{@code portCreator != null}
     * pre	{@code serverInboundPortURIs != null && serverInboundPortURIs.length > 0}
     * post	true			// no postcondition.
     * </pre>
     *
     * @param nbThreads					number of threads used to execute the client calls.
     * @param ownInboundPortURI			URI of the inbound port of this component.
     * @param selector					a function that selects outbound ports among the available ones.
     * @param mode						asynchronous call mode of the replication manager.
     * @param combinator				a function that combines results from servers to give one result returned to the caller.
     * @param portCreator				a port factory to create inbound and outbound ports.
     * @param serverInboundPortURIs		URIs of the inbound ports of the server to connect this component.
     * @throws Exception				<i>to do</i>.
     */
    protected			ReplicationManagerNonBlocking(
            int nbThreads,
            String ownInboundPortURI,
            SelectorI selector,
            CallMode mode,
            CombinatorI<T> combinator,
            PortFactoryI portCreator,
            String[] serverInboundPortURIs
    ) throws Exception
    {
        super(1, ownInboundPortURI, selector, combinator,
                portCreator, serverInboundPortURIs) ;

        assert	mode != null ;

        this.initialise(mode, nbThreads) ;
    }

    /**
     * creating an asynchronous replication manager.
     *
     * <p><strong>Description</strong></p>
     *
     * <p>
     * If the call mode is <code>ANY</code>, it is better to have
     * {@code nbThreads == serverInboundPortURIs.length} <i>i.e.</i>, the
     * number of threads is equal to the number of servers, so that all of
     * them have a chance to respond otherwise the "race" will be only among
     * the first bunch of servers called with the number of available threads.
     * If the call mode is <code>FIRST</code> (return the first result to come
     * in but let the other servers execute the request), the same may apply,
     * but not necessarily.
     * </p>
     * <p>
     * If the call mode is <code>SINGLE</code> or <code>ALL</code>, the number
     * of threads determines respectively the number of different requests or
     * copies of a request that can be executed in parallel.
     * </p>
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code nbThreads > 0}
     * pre	{@code ownInboundPortURI != null}
     * pre	{@code selector != null && ThreadSafe(selector)}
     * pre	{@code mode != null}
     * pre	{@code combinator != null && ThreadSafe(combinator)}
     * pre	{@code portCreator != null}
     * pre	{@code serverInboundPortURIs != null && serverInboundPortURIs.length > 0}
     * post	true			// no postcondition.
     * </pre>
     *
     * @param reflectionInboundPortURI	URI of the reflection inbound port of this component.
     * @param nbThreads					number of threads used to execute the client calls.
     * @param ownInboundPortURI			URI of the inbound port of this component.
     * @param selector					a function that selects outbound ports among the available ones.
     * @param mode						asynchronous call mode of the replication manager.
     * @param combinator				a function that combines results from servers to give one result returned to the caller.
     * @param portCreator				a port factory to create inbound and outbound ports.
     * @param serverInboundPortURIs		URIs of the inbound ports of the server to connect this component.
     * @throws Exception				<i>to do</i>.
     */
    protected			ReplicationManagerNonBlocking(
            String reflectionInboundPortURI,
            int nbThreads,
            String ownInboundPortURI,
            SelectorI selector,
            CallMode mode,
            CombinatorI<T> combinator,
            PortFactoryI portCreator,
            String[] serverInboundPortURIs
    ) throws Exception
    {
        super(reflectionInboundPortURI, 2, ownInboundPortURI,
                selector, combinator, portCreator, serverInboundPortURIs) ;

        assert	mode != null ;

        this.initialise(mode, nbThreads) ;
    }

    /**
     * initialise the replication manager.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true			// no precondition.
     * post	true			// no postcondition.
     * </pre>
     *
     * @param mode			asynchronous call mode of the replication manager.
     * @param nbThreads		number of threads used to execute the calls to the servers.
     */
    protected void		initialise(CallMode mode, int nbThreads)
    {
        this.callMode = mode ;
        this.createNewExecutorService(CALL_POOL_URI, nbThreads+3, false) ;
    }



    @SuppressWarnings("unchecked")
    @Override
    public T			call(Object... parameters) throws Exception
    {



        // This method is meant to be executed by the thread of the caller
        // component (the inbound port does not call handleRequest by directly
        // this method.

        // The method select must be thread safe.
        OutboundPortI[]	selected = this.selector.select(this.outboundPorts) ;

        // The next lines are for pedagogical and debugging purposes.
        StringBuffer mes = new StringBuffer() ; ;
        for (int i = 0 ; i < selected.length ; i++) {
            mes.append(this.numbers.get(selected[i])) ;
            if (i < selected.length - 1) {
                mes.append(" ,") ;
            }
        }
        mes.append("\n") ;
        this.traceMessage(mes.toString()) ;
        // The next lines create all of the request objects that will execute
        // the calls to the server components.

        for (int i = 0 ; i <  selected.length ; i++) {
            OutboundPortI p = selected[i] ;
            runTask(CALL_POOL_URI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(" envoi du message "+ parameters[1]);
                                ((ReplicableCI<T>)p).call(parameters) ;
                                System.out.println(" adeu "+ parameters[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        return (T)"ok";
    }

    /**
     * finish the computations after the first has returned its result to
     * release the semaphore only after all have finished or when an exception
     * has been raised.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code ecs != null}
     * pre	{@code ecs n >= 0}
     * post	true			// no postcondition.
     * </pre>
     *
     * @param ecs					the executor completion service to wait for the results.
     * @param n						the number of awaited results.
     * @throws ExecutionException	if one of the computation has thrown such an exception.
     */
    protected void		finishComputations(
            ExecutorCompletionService<T> ecs,
            int n
    ) throws ExecutionException
    {
        ExecutionException raised = null ;
        int count = 0 ;
        try {
            for (int i = 0 ; i < n ; i++) {
                ecs.take().get() ;
                count++ ;
            }
        } catch (InterruptedException e) {
            // In this case, a problem occurred with one or more servers,
            // so the replication manager should look at the problem and
            // try to recover.
        } catch (ExecutionException e) {
            // In this case, it is the execution of the call that raised
            // the exception, the caller must be informed.
            raised = e ;
        } finally {
            //this.s.release() ;
        }

        if (count < n && raised != null) throw raised ;
    }
}
