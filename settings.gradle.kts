/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific language
 * governing permissions and limitations under the Licence.
 */

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            mavenContent { snapshotsOnly() }
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "wallet"

fun cloneRepositoryIfNotExists(dirName: String, branch: String, url: String) {
    val dir = rootDir.resolve(dirName)
    if (!dir.exists() || (dir.isDirectory && dir.list().isNullOrEmpty())) {
        println("Directory '$dirName' not found or empty. Cloning $url (branch $branch)...")
        val exitCode = try {
            ProcessBuilder("git", "clone", "-b", branch, url, dirName)
                .directory(rootDir)
                .inheritIO()
                .start()
                .waitFor()
        } catch (e: Exception) {
            throw GradleException("Failed to execute git clone for '$dirName': ${e.message}", e)
        }
        if (exitCode != 0) {
            throw GradleException("Failed to clone $url (branch $branch) into '$dirName'. Exit code: $exitCode")
        }
    }
}

cloneRepositoryIfNotExists(
    dirName = "core",
    branch = "develop-0.30.2",
    url = "https://github.com/german-national-wallet/eudi-lib-android-wallet-core"
)
cloneRepositoryIfNotExists(
    dirName = "openid4vci",
    branch = "develop-0.13.1",
    url = "https://github.com/german-national-wallet/eudi-lib-jvm-openid4vci-kt"
)

include(":app")
include(":business-logic")
include(":ui-logic")
include(":network-logic")
include(":resources-logic")
include(":assembly-logic")
include(":startup-feature")
include(":test-logic")
include(":test-feature")
include(":common-feature")
include(":card-reader-feature")
include(":dashboard-feature")
include(":presentation-feature")
include(":issuance-feature")
include(":analytics-logic")
include(":baseline-profile")
include(":authentication-logic")
include(":core-logic")
include(":core:wallet-core")
include(":core:document-manager")
include(":core:transfer-manager")
include(":openid4vci")
include(":feature-flags")
include(":wallet-pin-feature")
include(":push-notifications-feature")
include(":storage-logic")
include(":revocation-feature")
include(":onboarding-feature")
