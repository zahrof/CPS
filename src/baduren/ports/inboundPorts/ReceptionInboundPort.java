package baduren.ports.inboundPorts;

import java.lang.annotation.Annotation;

import baduren.components.Broker;
import baduren.components.Subscriber;
import baduren.interfaces.MessageI;
import baduren.interfaces.ReceptionCI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ComponentStateI;
import fr.sorbonne_u.components.PluginI;
import fr.sorbonne_u.components.helpers.Logger;
import fr.sorbonne_u.components.helpers.TracerOnConsole;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.utils.ConstructorSignature;
import fr.sorbonne_u.components.reflection.utils.ServiceSignature;

public class ReceptionInboundPort extends		AbstractInboundPort implements ReceptionCI, ReflectionI {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Constructeurs
	 */
	
	public	ReceptionInboundPort(String uri, ComponentI owner) throws Exception{
		// the implemented interface is statically known
		super(uri, ReceptionCI.class, owner) ;
	}
	
	public	ReceptionInboundPort(ComponentI owner) throws Exception{
		// the implemented interface is statically known
		super(ReceptionCI.class, owner) ;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((Subscriber)this.owner).acceptMessage(m);
	}

	@Override
	public void acceptMessages(MessageI[] ms) {
		((Subscriber)this.owner).acceptMessages(ms);
	}

	@Override
	public boolean hasInstalledPlugins() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInstalled(String pluginURI) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PluginI getPlugin(String pluginURI) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInitialised(String pluginURI) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLogging() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTracing() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInStateAmong(ComponentStateI[] states) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean notInStateAmong(ComponentStateI[] states) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasItsOwnThreads() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTotalNUmberOfThreads() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasSerialisedExecution() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canScheduleTasks() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<?>[] getInterfaces() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?>[] getRequiredInterfaces() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getRequiredInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?>[] getOfferedInterfaces() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getOfferedInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequiredInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOfferedInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] findPortURIsFromInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findInboundPortURIsFromInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findOutboundPortURIsFromInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPortExisting(String portURI) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<?> getPortImplementedInterface(String portURI) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPortConnected(String portURI) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getComponentDefinitionClassName() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation[] getComponentAnnotations() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getComponentLoader() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceSignature[] getComponentServiceSignatures() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConstructorSignature[] getComponentConstructorSignatures() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void installPlugin(PluginI plugin) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finalisePlugin(String pluginURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uninstallPlugin(String pluginURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialisePlugin(String pluginURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLogger(Logger logger) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggleLogging() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logMessage(String message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printExecutionLog() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printExecutionLogOnFile(String fileName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTracer(TracerOnConsole tracer) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggleTracing() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void traceMessage(String message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRequiredInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRequiredInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOfferedInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeOfferedInterface(Class<?> inter) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPortConnection(String portURI, String otherPortURI, String ccname) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPortDisconnection(String portURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ComponentI newInstance(Object[] parameters) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeService(String name, Object[] params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeServiceSync(String name, Object[] params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invokeServiceAsync(String name, Object[] params) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertBeforeService(String methodName, String[] parametersCanonicalClassNames, String code)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAfterService(String methodName, String[] parametersCanonicalClassNames, String code)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
