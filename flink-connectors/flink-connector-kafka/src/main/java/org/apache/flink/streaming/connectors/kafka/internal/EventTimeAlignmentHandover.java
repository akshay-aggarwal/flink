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

	public List<TopicPartition> getPartitionsToPause() {
		synchronized (lock) {
			return this.partitionsToPause;
		}
	}

	public void activate(List<TopicPartition> partitionsToPause) {
		synchronized (lock) {
			this.partitionsToPause = partitionsToPause;
		}
	}

	public void deactivate() {
		synchronized (lock) {
			this.partitionsToPause = null;
		}
	}

}
