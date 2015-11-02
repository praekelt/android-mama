package za.foundation.praekelt.mama.inject.scope

import kotlin.annotation.Retention
import kotlin.annotation.AnnotationRetention
import javax.inject.Scope

/**
 * Dagger scopes
 */

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation public class ActivityScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation public class FragmentScope