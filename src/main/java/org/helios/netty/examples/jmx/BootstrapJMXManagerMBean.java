/**
 * Helios, OpenSource Monitoring
 * Brought to you by the Helios Development Group
 *
 * Copyright 2007, Helios Development Group and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org. 
 *
 */
package org.helios.netty.examples.jmx;

import java.util.Map;

/**
 * <p>Title: BootstrapJMXManagerMBean</p>
 * <p>Description: JMX MBean interface for {@link BootstrapJMXManager}</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.netty.examples.jmx.BootstrapJMXManagerMBean</code></p>
 */

public interface BootstrapJMXManagerMBean {
	/**
	 * Returns the map of channel options
	 * @return the map of channel options
	 */
	public Map<String, Object> getChannelOptions();
	
	/**
	 * Returns the option value for the passed option name
	 * @param name The name of the channel option
	 * @return The value of the channel option or null if it has not been set
	 */
	public Object getChannelOption(String name);
	
	/**
	 * Sets a channel option
	 * @param name The name of the option to set
	 * @param value The value to set the option to
	 */
	public void setChannelOption(String name, int value);
	
	/**
	 * Sets a channel option
	 * @param name The name of the option to set
	 * @param value The value to set the option to
	 */
	public void setChannelOption(String name, boolean value);
	
	/**
	 * Sets a channel option
	 * @param name The name of the option to set
	 * @param value The value to set the option to
	 */
	public void setChannelOption(String name, String value);

}
