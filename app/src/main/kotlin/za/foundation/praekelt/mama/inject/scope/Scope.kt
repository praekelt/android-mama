package za.foundation.praekelt.mama.inject.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Dagger scopes
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation public class ActivityScope

@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation public class FragmentScope