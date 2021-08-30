package dinson.customview.widget._010parallaxsplash;

/**
 * 视差动画播放时参数的控制
 */
class ParallaxViewTag {
    float xIn;
    float xOut;
    float yIn;
    float yOut;
    float alphaIn;
    float alphaOut;


    @Override
    public String toString() {
        return "ParallaxViewTag [xIn=" + xIn + ", xOut="
            + xOut + ", yIn=" + yIn + ", yOut=" + yOut + ", alphaIn="
            + alphaIn + ", alphaOut=" + alphaOut + "]";
    }

    boolean isBlank() {
        return xIn == 0 && xOut == 0 && yIn == 0 && yOut == 0 && alphaIn == 0 && alphaOut == 0;
    }
}
