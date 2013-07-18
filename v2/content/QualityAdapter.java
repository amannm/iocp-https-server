/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

/**
 *
 * @author admin
 */

//Wrapper for the Accept-* and TE headers
public class QualityAdapter<T> implements Comparable<QualityAdapter<T>> {

    //1000 = 1.000
    //0 = 0.000
    private T qualifiedType;
    private Integer qValue;

    @Override
    public int compareTo(QualityAdapter<T> other) {
        return qValue - other.qValue;
    }
}
