/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.api.LogoutsServiceI;
import org.laokou.admin.client.dto.logout.clientobject.LogoutCmd;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.trace.annotation.TraceLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laokou
 */
@RestController
@Tag(name = "LogoutsController", description = "退出")
@RequiredArgsConstructor
public class LogoutsController {

	private final LogoutsServiceI logoutsServiceI;

	@TraceLog
	@GetMapping("v1/logouts/{token}")
	@Operation(summary = "注销", description = "注销")
	public Result<Boolean> logout(@PathVariable("token")String token) {
		LogoutCmd cmd = new LogoutCmd();
		cmd.setToken(token);
		return logoutsServiceI.logout(cmd);
	}

}