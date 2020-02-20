package org.apache.flink.streaming.connectors.kafka.internal;

import org.apache.kafka.common.TopicPartition;

import java.util.List;

/**
 * The interface for KafkaFetcher and KafkaConsumerThread
 * to co-ordinate event time alignment.
 */
public class EventTimeAlignmentHandover {

	private final Object lock = new Object();

	/** The list of partitions to be paused for realignment, null if alignment is inactive. */
	private List<TopicPartition> partitionsToPause;

	/** Flag which tells if event-time alignment is active. */
	private boolean isActive;

	public List<TopicPartition> pollPartitionsToPause() {
		List<TopicPartition> temp;
		synchronized (lock) {
			temp = this.partitionsToPause;
			this.partitionsToPause = null;
		}
		return temp;
	}

	public void activate(List<TopicPartition> partitionsToPause) {
		synchronized (lock) {
			this.isActive = true;
			this.partitionsToPause = partitionsToPause;
		}
	}

	public void deactivate() {
		synchronized (lock) {
			this.isActive = false;
			this.partitionsToPause = null;
		}
	}

	public boolean isActive() {
		synchronized (lock) {
			return isActive;
		}
	}
}
