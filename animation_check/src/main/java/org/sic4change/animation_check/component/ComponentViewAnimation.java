package org.sic4change.animation_check.component;

import android.content.Context;
import android.view.View;

import org.sic4change.animation_check.animator.AnimationState;
import org.sic4change.animation_check.exception.NullStateListenerException;

/**
 * @author jlmd
 */
public abstract class ComponentViewAnimation extends View {

  protected final int parentWidth;
  protected final int mainColor;
  protected final int secondaryColor;

  protected float parentCenter;
  protected float circleRadius;
  protected int strokeWidth;
  private StateListener stateListener;

  public ComponentViewAnimation(Context context, int parentWidth, int mainColor,
      int secondaryColor) {
    super(context);
    this.parentWidth = parentWidth;
    this.mainColor = mainColor;
    this.secondaryColor = secondaryColor;
    init();
  }

  private void init() {
    hideView();
    circleRadius = parentWidth / 10;
    parentCenter = parentWidth / 2;
    strokeWidth = (12 * parentWidth) / 700;
  }

  public void hideView() {
    setVisibility(View.GONE);
  }

  public void showView() {
    setVisibility(View.VISIBLE);
  }

  public void setState(AnimationState state) {
    if (stateListener != null) {
      stateListener.onStateChanged(state);
    } else {
      throw new NullStateListenerException();
    }
  }

  public void setStateListener(StateListener stateListener) {
    this.stateListener = stateListener;
  }

  public interface StateListener {

    void onStateChanged(AnimationState state);
  }
}
