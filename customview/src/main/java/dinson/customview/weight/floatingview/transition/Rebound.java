


package dinson.customview.weight.floatingview.transition;


import com.facebook.rebound.Spring;

public interface Rebound {
    
    public Spring createSpringByBouncinessAndSpeed(double bounciness, double speed);

    public Spring createSpringByTensionAndFriction(double tension, double friction) ;

    public float transition(double progress, float startValue, float endValue);
    
}
