package uk.ac.warwick.dcs.cs261.team14.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.warwick.dcs.cs261.team14.data.pipeline.DataPipelineModel;
import uk.ac.warwick.dcs.cs261.team14.data.pipeline.InputController;
import uk.ac.warwick.dcs.cs261.team14.db.entities.SymbolRepository;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Ming on 2/12/2017.
 */

@Component
public class IngestorController implements Serializable {
    @Autowired
    private LiveStreamTask liveStreamTask;

    @Autowired
    private InputController inputController;

    @Autowired
    private DataPipelineModel dataPipelineModel;

    @Autowired
    private SymbolRepository symbolRepository;

    public void acceptFile(String path) {
        File file = new File(path);

        inputController.processFile(file);
    }

    public LiveStreamTask getLiveStreamTask() {
        return liveStreamTask;
    }
}
