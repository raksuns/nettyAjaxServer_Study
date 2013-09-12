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
package org.helios.netty.jmx;

/**
 * <p>Title: MetricCollectorMXBean</p>
 * <p>Description: The MXBean interface for the {@link MetricCollector}</p> 
 * <p>Company: Helios Development Group LLC</p>
 * @author Whitehead (nwhitehead AT heliosdev DOT org)
 * <p><code>org.helios.netty.jmx.MetricCollectorMXBean</code></p>
 */
public interface MetricCollectorMXBean {
	/**
	 * Returns a JSON string of all the registered metric names 
	 * @return the metricNames
	 */
	public String getMetricNames();
	
	/**
	 * Submits a new metric and value
	 * @param metricName The metric name
	 * @param value The metric value
	 */
	public void submitMetric(String metricName, long value);
	
	/**
	 * Returns the number of dropped metrics
	 * @return the number of dropped metrics
	 */
	public long getDroppedMetricCount();
}
