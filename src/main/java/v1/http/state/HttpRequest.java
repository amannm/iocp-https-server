/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v1.http.state;

import v1.http.util.constants.HttpProtocol;
import v1.http.util.constants.HttpRequestMethod;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class HttpRequest {

    private HttpRequestMethod method;
    private URI path;
    private HttpProtocol protocol;
    private Map<String, String[]> headerMap = new HashMap<>();
    private String content;

    public HttpRequestMethod getMethod() {
        return method;
    }

    public void setMethod(HttpRequestMethod method) {
        this.method = method;
    }

    public URI getPath() {
        return path;
    }

    public void setPath(URI path) {
        this.path = path;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(HttpProtocol protocol) {
        this.protocol = protocol;
    }

    public Map<String, String[]> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String[]> headerMap) {
        this.headerMap = headerMap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(method).append("]").append("[").append(path).append("]").append("[").append(protocol).append("]\n");
        for (Map.Entry<String, String[]> entry : headerMap.entrySet()) {
            sb.append("[").append(entry.getKey()).append("]").append(" = ");
            String[] values = entry.getValue();
            if (values != null) {
                if (values.length != 0) {
                    sb.append("{").append(values[0]);
                    for (int i = 1; i < values.length; i++) {
                        sb.append(" | ").append(values[i]);
                    }
                    sb.append("}");
                }
            }
            sb.append("\n");
        }
        if (content != null) {
            sb.append(content);
        }
        return sb.toString();

    }
}
