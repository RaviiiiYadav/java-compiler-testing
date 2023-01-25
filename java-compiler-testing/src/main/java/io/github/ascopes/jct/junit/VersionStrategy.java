/*
 * Copyright (C) 2022 - 2023, the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ascopes.jct.junit;

import io.github.ascopes.jct.compilers.JctCompiler;
import java.util.function.BiConsumer;
import javax.annotation.concurrent.ThreadSafe;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Strategy for setting a version on a JUnit compiler annotation.
 *
 * @author Ashley Scopes
 * @since 0.0.1 (0.0.1-M7)
 */
@API(since = "0.0.1", status = Status.STABLE)
@ThreadSafe
public enum VersionStrategy {

  /**
   * Set the {@link JctCompiler#release release}.
   */
  RELEASE(
      JctCompiler::release,
      (compiler, version) -> compiler
          .name(compiler.getName() + " (release = Java " + version + ")")
  ),

  /**
   * Set the {@link JctCompiler#source} source}.
   */
  SOURCE(
      JctCompiler::source,
      (compiler, version) -> compiler
          .name(compiler.getName() + " (source = Java " + version + ")")
  ),

  /**
   * Set the {@link JctCompiler#target} target}.
   */
  TARGET(
      JctCompiler::target,
      (compiler, version) -> compiler
          .name(compiler.getName() + " (target = Java " + version + ")")
  ),

  /**
   * Set the {@link JctCompiler#source source} and {@link JctCompiler#target target}.
   */
  SOURCE_AND_TARGET(
      (compiler, version) -> compiler
          .source(version)
          .target(version),
      (compiler, version) -> compiler
          .name(compiler.getName() + " (source and target = Java " + version + ")")
  );

  private final BiConsumer<JctCompiler<?, ?>, Integer> versionSetter;
  private final BiConsumer<JctCompiler<?, ?>, Integer> descriptionFormatter;

  VersionStrategy(
      BiConsumer<JctCompiler<?, ?>, Integer> versionSetter,
      BiConsumer<JctCompiler<?, ?>, Integer> descriptionFormatter
  ) {
    this.versionSetter = versionSetter;
    this.descriptionFormatter = descriptionFormatter;
  }

  /**
   * Set the given version on the compiler, according to the strategy in use.
   *
   * @param compiler the compiler to configure.
   * @param version  the version to set.
   */
  public void configureCompiler(JctCompiler<?, ?> compiler, int version) {
    versionSetter.accept(compiler, version);
    descriptionFormatter.accept(compiler, version);
  }
}
