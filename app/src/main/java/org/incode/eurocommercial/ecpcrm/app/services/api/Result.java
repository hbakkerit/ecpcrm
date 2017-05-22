/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.app.services.api;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Result {
    private final int status;
    private final String message;
    private final Object response;

    public static Result error(final int status, final String message) {
        return new Result(status, message, null);
    }
    public static Result ok(Object viewModel) {
        return new Result(200, null, viewModel);
    }
    public static Result ok() {
        return ok(null);
    }
}
