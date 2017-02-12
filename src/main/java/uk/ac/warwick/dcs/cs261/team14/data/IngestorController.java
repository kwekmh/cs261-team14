package uk.ac.warwick.dcs.cs261.team14.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.data.pipeline.InputController;

import java.io.File;

/**
 * Created by Ming on 2/12/2017.
 */

@Component
public class IngestorController {
    private LiveStreamTask liveStreamTask;

    @Autowired
    private InputController inputController;

    public IngestorController() {
        liveStreamTask = new LiveStreamTask();
    }

    public void acceptFile(String path) {
        File file = new File(path);

        inputController.processFile(file);
    }

    public LiveStreamTask getLiveStreamTask() {
        return liveStreamTask;
    }
}
