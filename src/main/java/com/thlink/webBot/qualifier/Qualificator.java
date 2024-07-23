package com.thlink.webBot.qualifier;

import com.thlink.webBot.qualifier.Metrics.Metric1;
import com.thlink.webBot.qualifier.Metrics.Metric2;
import com.thlink.webBot.qualifier.Metrics.Metric3;
import org.apache.log4j.Logger;

public class Qualificator {
    private static final Logger logger = Logger.getLogger(Qualificator.class);
      
    public QualificationResult evaluateModel (ConversationModel pModel)
    {
        logger.info("Qualificator.evaluateModel");
        QualificationResult result = new QualificationResult();
        result.setClient(pModel.getClient());
        logger.info("evalMetric1");
        result.setScoreM1(new Metric1().evalMetric1(pModel));
        logger.info("evalMetric2");
        result.setScoreM2(new Metric2().evalMetric2(pModel));
        logger.info("evalMetric3");
        result.setScoreM3(new Metric3().evalMetric3(pModel));
        logger.info("evalMetric DONE!");
        return result;
    }
}
