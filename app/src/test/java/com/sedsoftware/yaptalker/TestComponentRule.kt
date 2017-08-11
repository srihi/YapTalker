package com.sedsoftware.yaptalker

import android.support.annotation.NonNull
import com.sedsoftware.yaptalker.di.ApplicationComponent
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestComponentRule : TestRule {

  var testComponent: ApplicationComponent

  constructor() {
    testComponent = TestComponent()
  }

  constructor(@NonNull component: ApplicationComponent) {
    this.testComponent = component
  }

  override fun apply(base: Statement?, description: Description?): Statement {
    return object : Statement() {
      @Throws(Throwable::class)
      override fun evaluate() {
        YapTalkerApp.appComponent = testComponent
        base?.evaluate()
      }
    }
  }
}