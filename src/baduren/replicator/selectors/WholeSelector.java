package baduren.replicator.selectors;

import baduren.replicator.interfaces.SelectorI;
import fr.sorbonne_u.components.ports.OutboundPortI;

// -----------------------------------------------------------------------------

/**
 * The class <code>IdentitySelector</code>
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
public class			WholeSelector
implements SelectorI
{

	@Override
	public synchronized OutboundPortI[]	select(OutboundPortI[] ports)
	{
		assert	ports != null && ports.length > 0 ;
		return ports ;
	}
}
// -----------------------------------------------------------------------------
