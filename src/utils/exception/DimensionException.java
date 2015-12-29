/*
 * Dimension Exception thrown with invalid dimensions. (Empty dimension or negative dimension)
 */

package utils.exception;

/**
 *
 * @author TonyTsang
 */
public class DimensionException extends RuntimeException {
    private static final long serialVersionUID = 3458703424484480967L;

    public DimensionException() {
        super();
    }

    public DimensionException(String message) {
        super(message);
    }

    public DimensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DimensionException(Throwable cause) {
        super(cause);
    }
}
