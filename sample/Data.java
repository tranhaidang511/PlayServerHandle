package exp4server.sample;

import java.io.Serializable;

/**
 * シリアライズのサンプル．
 * シリアライズされるオブジェクトはSerializableを実装すること．
 */
public class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String sex;

    public String name;

    public String description;
    
    @Override
    public String toString() {
        return sex + ":" + name + ":" + description;
    }
}
