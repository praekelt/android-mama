package za.foundation.praekelt.mama.inject.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * Dagger scopes
 */

Qualifier
Retention(RetentionPolicy.RUNTIME)
annotation public class ApplicationScope

Qualifier
Retention(RetentionPolicy.RUNTIME)
annotation public class FragmentScope