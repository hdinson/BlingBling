

package dinson.customview.widget._029floatingview.transition;



public abstract  class BaseFloatingPathTransition  implements FloatingPathTransition {

    private PathPosition mPathPosition;
    private float [] mPathPositionGetter;
    
    public float getStartPathPosition(){
        return 0;
    }
    
    public float getEndPathPosition(){
        if (getFloatingPath() != null){
            return getFloatingPath().getPathMeasure().getLength();
        }
        return 0;
    }
    
    public PathPosition getFloatingPosition(float progress) {
        if (mPathPosition == null){
            mPathPosition = new PathPosition();
        }
        if (mPathPositionGetter == null){
            mPathPositionGetter = new float[2];
        }
        if (getFloatingPath() != null){
            getFloatingPath() .getPathMeasure().getPosTan(progress, mPathPositionGetter, null);
            mPathPosition.x = mPathPositionGetter[0];
            mPathPosition.y = mPathPositionGetter[1];
        }
        return mPathPosition;
    }
    
    
    
}
