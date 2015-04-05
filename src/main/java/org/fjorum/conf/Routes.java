/**
 * Copyright (C) 2012 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fjorum.conf;


import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import org.fjorum.annotation.Scanner;
import org.fjorum.controllers.*;

@SuppressWarnings("unused")
public class Routes implements ApplicationRoutes, Scanner {

    @Override
    public void init(Router router) {


        //router.GET().route("/user/activate/{activationCode}").with(UserController.class, "activateUser");
        //router.GET().route("/user/request_reset_password").with(UserController.class, "requestResetPassword");
        //router.POST().route("/user/request_reset_password").with(UserController.class, "requestResetPasswordPost");
        //router.GET().route("/user/reset_password/{passwordResetCode}").with(UserController.class, "resetPassword");
        //router.POST().route("/user/reset_password").with(UserController.class, "resetPasswordPost");

        scan(router,
                IndexController.class,
                ForumController.class,
                ProfileController.class,
                AdminController.class,
                ErrorController.class);

        // Assets (pictures / javascript)
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsController.class, "serveWebJars");
        router.GET().route("/assets/{fileName: .*}").with(AssetsController.class, "serveStatic");

        // Index / Catchall shows index page
        router.GET().route("/.*").with(IndexController.class, "index");
    }

}
