package abi46_0_0.host.exp.exponent.modules.api.screens

import abi46_0_0.com.facebook.react.bridge.JSApplicationIllegalArgumentException
import abi46_0_0.com.facebook.react.bridge.ReadableMap
import abi46_0_0.com.facebook.react.common.MapBuilder
import abi46_0_0.com.facebook.react.module.annotations.ReactModule
import abi46_0_0.com.facebook.react.uimanager.ThemedReactContext
import abi46_0_0.com.facebook.react.uimanager.ViewGroupManager
import abi46_0_0.com.facebook.react.uimanager.ViewManagerDelegate
import abi46_0_0.com.facebook.react.uimanager.annotations.ReactProp
import abi46_0_0.com.facebook.react.viewmanagers.RNSScreenManagerDelegate
import abi46_0_0.com.facebook.react.viewmanagers.RNSScreenManagerInterface
import abi46_0_0.host.exp.exponent.modules.api.screens.events.HeaderBackButtonClickedEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.ScreenAppearEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.ScreenDisappearEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.ScreenDismissedEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.ScreenTransitionProgressEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.ScreenWillAppearEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.ScreenWillDisappearEvent
import abi46_0_0.host.exp.exponent.modules.api.screens.events.StackFinishTransitioningEvent

@ReactModule(name = ScreenViewManager.REACT_CLASS)
class ScreenViewManager : ViewGroupManager<Screen>(), RNSScreenManagerInterface<Screen> {
  private val mDelegate: ViewManagerDelegate<Screen>

  init {
    mDelegate = RNSScreenManagerDelegate<Screen, ScreenViewManager>(this)
  }

  override fun getName(): String {
    return REACT_CLASS
  }

  override fun createViewInstance(reactContext: ThemedReactContext): Screen {
    return Screen(reactContext)
  }

  override fun setActivityState(view: Screen, activityState: Float) {
    setActivityState(view, activityState.toInt())
  }

  @ReactProp(name = "activityState")
  fun setActivityState(view: Screen, activityState: Int) {
    if (activityState == -1) {
      // Null will be provided when activityState is set as an animated value and we change
      // it from JS to be a plain value (non animated).
      // In case when null is received, we want to ignore such value and not make
      // any updates as the actual non-null value will follow immediately.
      return
    }
    when (activityState) {
      0 -> view.setActivityState(Screen.ActivityState.INACTIVE)
      1 -> view.setActivityState(Screen.ActivityState.TRANSITIONING_OR_BELOW_TOP)
      2 -> view.setActivityState(Screen.ActivityState.ON_TOP)
    }
  }

  @ReactProp(name = "stackPresentation")
  override fun setStackPresentation(view: Screen, presentation: String?) {
    view.stackPresentation = when (presentation) {
      "push" -> Screen.StackPresentation.PUSH
      "modal", "containedModal", "fullScreenModal", "formSheet" ->
        Screen.StackPresentation.MODAL
      "transparentModal", "containedTransparentModal" ->
        Screen.StackPresentation.TRANSPARENT_MODAL
      else -> throw JSApplicationIllegalArgumentException("Unknown presentation type $presentation")
    }
  }

  @ReactProp(name = "stackAnimation")
  override fun setStackAnimation(view: Screen, animation: String?) {
    view.stackAnimation = when (animation) {
      null, "default", "flip", "simple_push" -> Screen.StackAnimation.DEFAULT
      "none" -> Screen.StackAnimation.NONE
      "fade" -> Screen.StackAnimation.FADE
      "slide_from_right" -> Screen.StackAnimation.SLIDE_FROM_RIGHT
      "slide_from_left" -> Screen.StackAnimation.SLIDE_FROM_LEFT
      "slide_from_bottom" -> Screen.StackAnimation.SLIDE_FROM_BOTTOM
      "fade_from_bottom" -> Screen.StackAnimation.FADE_FROM_BOTTOM
      else -> throw JSApplicationIllegalArgumentException("Unknown animation type $animation")
    }
  }

  @ReactProp(name = "gestureEnabled", defaultBoolean = true)
  override fun setGestureEnabled(view: Screen, gestureEnabled: Boolean) {
    view.isGestureEnabled = gestureEnabled
  }

  @ReactProp(name = "replaceAnimation")
  override fun setReplaceAnimation(view: Screen, animation: String?) {
    view.replaceAnimation = when (animation) {
      null, "pop" -> Screen.ReplaceAnimation.POP
      "push" -> Screen.ReplaceAnimation.PUSH
      else -> throw JSApplicationIllegalArgumentException("Unknown replace animation type $animation")
    }
  }

  @ReactProp(name = "screenOrientation")
  override fun setScreenOrientation(view: Screen, screenOrientation: String?) {
    view.setScreenOrientation(screenOrientation)
  }

  @ReactProp(name = "statusBarAnimation")
  override fun setStatusBarAnimation(view: Screen, statusBarAnimation: String?) {
    val animated = statusBarAnimation != null && "none" != statusBarAnimation
    view.isStatusBarAnimated = animated
  }

  @ReactProp(name = "statusBarColor", customType = "Color")
  override fun setStatusBarColor(view: Screen, statusBarColor: Int?) {
    view.statusBarColor = statusBarColor
  }

  @ReactProp(name = "statusBarStyle")
  override fun setStatusBarStyle(view: Screen, statusBarStyle: String?) {
    view.statusBarStyle = statusBarStyle
  }

  @ReactProp(name = "statusBarTranslucent")
  override fun setStatusBarTranslucent(view: Screen, statusBarTranslucent: Boolean) {
    view.isStatusBarTranslucent = statusBarTranslucent
  }

  @ReactProp(name = "statusBarHidden")
  override fun setStatusBarHidden(view: Screen, statusBarHidden: Boolean) {
    view.isStatusBarHidden = statusBarHidden
  }

  @ReactProp(name = "navigationBarColor", customType = "Color")
  override fun setNavigationBarColor(view: Screen, navigationBarColor: Int?) {
    view.navigationBarColor = navigationBarColor
  }

  @ReactProp(name = "navigationBarHidden")
  override fun setNavigationBarHidden(view: Screen, navigationBarHidden: Boolean) {
    view.isNavigationBarHidden = navigationBarHidden
  }

  @ReactProp(name = "nativeBackButtonDismissalEnabled")
  override fun setNativeBackButtonDismissalEnabled(
    view: Screen,
    nativeBackButtonDismissalEnabled: Boolean
  ) {
    view.nativeBackButtonDismissalEnabled = nativeBackButtonDismissalEnabled
  }

  // these props are not available on Android, however we must override their setters
  override fun setFullScreenSwipeEnabled(view: Screen?, value: Boolean) = Unit

  override fun setTransitionDuration(view: Screen?, value: Int) = Unit

  override fun setHideKeyboardOnSwipe(view: Screen?, value: Boolean) = Unit

  override fun setCustomAnimationOnSwipe(view: Screen?, value: Boolean) = Unit

  override fun setGestureResponseDistance(view: Screen?, value: ReadableMap?) = Unit

  override fun setHomeIndicatorHidden(view: Screen?, value: Boolean) = Unit

  override fun setPreventNativeDismiss(view: Screen?, value: Boolean) = Unit

  override fun setSwipeDirection(view: Screen?, value: String?) = Unit

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    val map: MutableMap<String, Any> = MapBuilder.of(
      ScreenDismissedEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onDismissed"),
      ScreenWillAppearEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onWillAppear"),
      ScreenAppearEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onAppear"),
      ScreenWillDisappearEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onWillDisappear"),
      ScreenDisappearEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onDisappear"),
      StackFinishTransitioningEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onFinishTransitioning"),
      ScreenTransitionProgressEvent.EVENT_NAME,
      MapBuilder.of("registrationName", "onTransitionProgress")
    )
    // there is no `MapBuilder.of` with more than 7 items
    map[HeaderBackButtonClickedEvent.EVENT_NAME] = MapBuilder.of("registrationName", "onHeaderBackButtonClicked")
    return map
  }

  protected override fun getDelegate(): ViewManagerDelegate<Screen> {
    return mDelegate
  }

  companion object {
    const val REACT_CLASS = "RNSScreen"
  }
}
