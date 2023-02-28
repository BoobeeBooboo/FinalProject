/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
	`kotlin-dsl`
}

group = "com.shimi.petep.buildlogic"

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
	compileOnly(libs.android.gradlePlugin)
	compileOnly(libs.kotlin.gradlePlugin)
	compileOnly(libs.firebase.performance.gradle)
	compileOnly(libs.firebase.crashlytics.gradle)
	compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
	plugins {
		register("androidApplicationCompose") {
			id = "petep.android.application.compose"
			implementationClass = "AndroidApplicationComposeConventionPlugin"
		}
		register("androidApplication") {
			id = "petep.android.application"
			implementationClass = "AndroidApplicationConventionPlugin"
		}
		register("androidApplicationJacoco") {
			id = "petep.android.application.jacoco"
			implementationClass = "AndroidApplicationJacocoConventionPlugin"
		}
		register("androidLibraryCompose") {
			id = "petep.android.library.compose"
			implementationClass = "AndroidLibraryComposeConventionPlugin"
		}
		register("androidLibrary") {
			id = "petep.android.library"
			implementationClass = "AndroidLibraryConventionPlugin"
		}
		register("androidFeature") {
			id = "petep.android.feature"
			implementationClass = "AndroidFeatureConventionPlugin"
		}
		register("androidLibraryJacoco") {
			id = "petep.android.library.jacoco"
			implementationClass = "AndroidLibraryJacocoConventionPlugin"
		}
		register("androidTest") {
			id = "petep.android.test"
			implementationClass = "AndroidTestConventionPlugin"
		}
		register("androidHilt") {
			id = "petep.android.hilt"
			implementationClass = "AndroidHiltConventionPlugin"
		}
		register("androidRoom") {
			id = "petep.android.room"
			implementationClass = "AndroidRoomConventionPlugin"
		}
		register("androidFirebase") {
			id = "petep.android.application.firebase"
			implementationClass = "AndroidApplicationFirebaseConventionPlugin"
		}
		register("androidFlavors") {
			id = "petep.android.application.flavors"
			implementationClass = "AndroidApplicationFlavorsConventionPlugin"
		}
	}
}
