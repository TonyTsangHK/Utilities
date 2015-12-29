package utils.data;

import utils.math.MathUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2015-06-12
 * Time: 10:53
 */
public class Version implements Comparable<Version> {
    public static final Version EMPTY_VERSION = new Version(0, 0, 0);

    private int major, minor, patches;

    public Version(String versionStr) {
        if (versionStr == null) {
            initialize(0, 0, 0);
        }
        String[] parts = versionStr.split("\\.");

        if (parts.length == 0) {
            initialize(0, 0, 0);
        } else if (parts.length == 1) {
            initialize(MathUtil.parseInt(parts[0], 10, 0), 0, 0);
        } else if (parts.length == 2) {
            initialize(MathUtil.parseInt(parts[0], 10, 0), MathUtil.parseInt(parts[1], 10, 0), 0);
        } else {
            initialize(
                MathUtil.parseInt(parts[0], 10, 0),
                MathUtil.parseInt(parts[1], 10, 0),
                MathUtil.parseInt(parts[2], 10, 0)
            );
        }
    }

    public Version(int major, int minor, int patches) {
        initialize(major, minor, patches);
    }

    private void initialize(int major, int minor, int patches) {
        this.major = major;
        this.minor = minor;
        this.patches = patches;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatches() {
        return patches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;

        Version version = (Version) o;

        return major == version.major && minor == version.minor && patches == version.patches;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + patches;
        return result;
    }

    @Override
    public int compareTo(Version o) {
        if (o == null) {
            return 1;
        } else {
            if (major == o.major) {
                if (minor == o.minor) {
                    if (patches == o.patches) {
                        return 0;
                    } else if (patches < o.patches) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (minor < o.minor) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (major < o.major) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patches;
    }

    public boolean isEmpty() {
        return major == 0 && minor == 0 && patches == 0;
    }
}
