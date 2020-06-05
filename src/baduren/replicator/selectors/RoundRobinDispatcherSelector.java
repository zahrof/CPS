package baduren.replicator.selectors;

import baduren.replicator.interfaces.SelectorI;
import fr.sorbonne_u.components.ports.OutboundPortI;

// -----------------------------------------------------------------------------

/**
 * The class <code>RoundRobinDispatcherSelector</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-02-28</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RoundRobinDispatcherSelector
implements SelectorI
{
	protected int		numberOfPorts ;
	protected int		next ;

	public				RoundRobinDispatcherSelector(int numberOfPorts)
	{
		assert numberOfPorts > 0 ;
		this.numberOfPorts = numberOfPorts ;
		this.next = 0 ;
	}

	public synchronized int			getNumberOfPorts()
	{
		return this.numberOfPorts ;
	}

	/**
	 * @see fr.sorbonne_u.alasca.replication.interfaces.SelectorI#select(OutboundPortI[])
	 */
	@Override
	public synchronized OutboundPortI[]	select(OutboundPortI[] ports)
	{
		assert	ports.length == this.getNumberOfPorts() ;
		OutboundPortI p = ports[this.next] ;
		this.next = (this.next + 1) % this.numberOfPorts ;
		return new OutboundPortI[]{p} ;
	}
}
// -----------------------------------------------------------------------------
