package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IRecommendation;
import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Model.User;
import com.unipi.msc.riseupapi.Repository.TaskRepository;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.collection.CollectionRecordReader;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.writable.NDArrayWritable;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendationService implements IRecommendation {
    private final ResourceLoader resourceLoader;
    private final TaskRepository taskRepository;

    @Override
    public void train() {
        // Load your dataset and preprocess it
        // Assuming you have a DataSetIterator for your data
        DataSetIterator dataSetIterator = getDataSetIterator();

        // Define and configure the neural network architecture
        MultiLayerNetwork model = buildModel();
        model.init();

        // Train the model
        int numEpochs = 10;
        for (int epoch = 0; epoch < numEpochs; epoch++) {
            while (dataSetIterator.hasNext()) {
                DataSet dataSet = dataSetIterator.next();
                model.fit(dataSet);
            }
            dataSetIterator.reset();
        }

        // Evaluate the model
        Evaluation evaluation = model.evaluate(dataSetIterator);
        System.out.println("Evaluation metrics: " + evaluation.stats());
        saveModel(model);
    }
    @Override
    public List<Long> recommendUsers(Long tagId, Long difficultyId) {
        List<Long> userIds = new ArrayList<>();
        MultiLayerNetwork model = loadModel();
        if (model == null) return userIds;
        // Make predictions for a given [TagId, DifficultyId]
        INDArray input = Nd4j.create(new double[]{tagId, difficultyId});
        INDArray output = model.output(input);
        // Get the index of the maximum probability
        Long predictedClassIndex = Nd4j.argMax(output, 1).getLong(0);
        userIds.add(Nd4j.argMax(output, 1).getLong(0));
        userIds.add(Nd4j.argMax(output, 1).getLong(1));
        return userIds;
    }

    private MultiLayerNetwork loadModel() {
        try {
            return ModelSerializer.restoreMultiLayerNetwork(new File(getModelPath()));
        } catch (IOException ignore) {}
        return null;
    }

    private void saveModel(MultiLayerNetwork model) {
        try {
            ModelSerializer.writeModel(model, new File(getModelPath()), true);
        } catch (IOException e) {
        }
    }

    private DataSetIterator getDataSetIterator() {
        List<Task> tasks = taskRepository.findAll();

        List<List<Writable>> data = new ArrayList<>();
        for (Task task : tasks) {
            for (User user:task.getUsers()){
                List<Writable> record = new ArrayList<>();
                record.add(new NDArrayWritable(Nd4j.scalar(task.getId())));
                record.add(new NDArrayWritable(Nd4j.scalar(task.getDifficulty().ordinal())));
                record.add(new NDArrayWritable(Nd4j.scalar(user.getId())));
                data.add(record);
            }
        }

        // Initialize the CollectionRecordReader with the schema and data
        RecordReader recordReader = new CollectionRecordReader(data);

        // Create the DataSetIterator
        return new RecordReaderDataSetIterator.Builder(recordReader,  64)
                .classification(0, 1)  // Assuming columns 0 and 1 are the input features (TagId, DifficultyId)
                .build();
    }


    private static MultiLayerNetwork buildModel() {
        int numInputFeatures = 2; // TagId, DifficultyId
        int numOutputClasses = 1; // UserId

        MultiLayerConfiguration builder = new NeuralNetConfiguration.Builder()
                .seed(123)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(1e-3))
                .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
                .gradientNormalizationThreshold(1.0)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(numInputFeatures)
                        .nOut(64)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nOut(32)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nOut(numOutputClasses)
                        .activation(Activation.IDENTITY)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(builder);
        model.setListeners(new ScoreIterationListener(10));

        return model;
    }
    private String getModelPath() throws IOException {
        return resourceLoader.getResource("classpath:model.zip").getFile().getAbsolutePath();
    }
}
