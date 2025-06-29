/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.common.log.handler;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.laokou.common.log.convertor.OperateLogConvertor;
import org.laokou.common.log.handler.event.OperateEvent;
import org.laokou.common.log.mapper.OperateLogMapper;
import org.laokou.common.mybatisplus.util.TransactionalUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.log.model.MqEnum.OPERATE_LOG_CONSUMER_GROUP;
import static org.laokou.common.log.model.MqEnum.OPERATE_LOG_TOPIC;
import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;

/**
 * @author laokou
 */

@Component
@RequiredArgsConstructor
public class OperateEventHandler {

	private final OperateLogMapper operateLogMapper;

	private final TransactionalUtils transactionalUtils;

	@KafkaListener(topics = OPERATE_LOG_TOPIC, groupId = OPERATE_LOG_CONSUMER_GROUP)
	public void handleOperateLog(List<ConsumerRecord<String, Object>> messages, Acknowledgment acknowledgment) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			for (ConsumerRecord<String, Object> record : messages) {
				transactionalUtils.executeInTransaction(
						() -> operateLogMapper.insert(OperateLogConvertor.toDataObject((OperateEvent) record.value())));
			}
		}
		finally {
			acknowledgment.acknowledge();
			DynamicDataSourceContextHolder.clear();
		}
	}

}
