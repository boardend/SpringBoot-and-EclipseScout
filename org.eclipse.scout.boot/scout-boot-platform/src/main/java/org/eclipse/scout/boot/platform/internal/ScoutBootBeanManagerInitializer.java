package org.eclipse.scout.boot.platform.internal;

import org.eclipse.scout.boot.platform.ScoutBootPlatform;
import org.eclipse.scout.rt.platform.Platform;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Starts the Scout platform which delegates to Spring bean manager. This class is invoked upon the initialization of
 * the Spring bean manager.
 * <p>
 * This class is discovered by Spring bean manager automatically because annotated with {@link Component}.
 */
@Component
public class ScoutBootBeanManagerInitializer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

  private ConfigurableApplicationContext applicationContext;

  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) {
    // NOOP
  }

  @Override
  public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) {
    ((ScoutBootPlatform) Platform.get()).start(applicationContext);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = (ConfigurableApplicationContext) applicationContext;
  }
}
