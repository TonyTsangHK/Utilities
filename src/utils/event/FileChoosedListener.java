package utils.event;

import java.util.EventListener;

public interface FileChoosedListener extends EventListener {
    public void fileChoosed(FileChoosedEvent evt);
}
