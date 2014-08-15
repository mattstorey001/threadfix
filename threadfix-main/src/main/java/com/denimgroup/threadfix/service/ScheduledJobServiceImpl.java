package com.denimgroup.threadfix.service;

import com.denimgroup.threadfix.data.dao.ScheduledJobDao;
import com.denimgroup.threadfix.data.entities.DayInWeek;
import com.denimgroup.threadfix.data.entities.ScheduledFrequencyType;
import com.denimgroup.threadfix.data.entities.ScheduledJob;
import com.denimgroup.threadfix.data.entities.ScheduledPeriodType;
import com.denimgroup.threadfix.logging.SanitizedLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Created by zabdisubhan on 8/15/14.
 */

@Service
@Transactional(readOnly = false)
public abstract class ScheduledJobServiceImpl<S extends ScheduledJob> implements ScheduledJobService<S> {

    private final SanitizedLogger log = new SanitizedLogger(ScheduledJobServiceImpl.class);

    protected abstract ScheduledJobDao<S> getScheduledJobDao();

    @Override
    public void validateDate(S scheduledJob, BindingResult result) {

        int hour = scheduledJob.getHour();
        int minute = scheduledJob.getMinute();
        String period = scheduledJob.getPeriod();
        String day = scheduledJob.getDay();
        String frequency = scheduledJob.getFrequency();

        if (result.hasFieldErrors("hour") || hour<0 || hour>12) {
            result.rejectValue("dateError", null, null, "Input hour as a number from 0 to 12");

            return;
        }
        if (result.hasFieldErrors("minute") || minute<0 || minute>59) {
            result.rejectValue("dateError", null, null, "Input minute as a number from 0 to 59");
            return;
        }
        if (result.hasFieldErrors("period") || ScheduledPeriodType.getPeriod(period)==null) {
            result.rejectValue("dateError", null, null, "Select AM or PM");
            return;
        }

        if (ScheduledFrequencyType.getFrequency(frequency) == ScheduledFrequencyType.WEEKLY
                && DayInWeek.getDay(day)==null) {
            result.rejectValue("dateError", null, null, "Select day from list");
        }

        // Clean day if it is Daily schedule
        if (ScheduledFrequencyType.getFrequency(frequency) == ScheduledFrequencyType.DAILY) {
            scheduledJob.setDay(null);
        }
    }

    @Override
    public int save(S scheduledJob) {
        getScheduledJobDao().saveOrUpdate(scheduledJob);
        int scheduledJobId = scheduledJob.getId();

        log.info("Created ScheduledJob with id: " + scheduledJobId);

        return scheduledJobId;
    }

    @Override
    public String delete(S scheduledJob) {
        log.info("Deleting scheduled job");

        getScheduledJobDao().delete(scheduledJob);
        return null;
    }

    @Override
    public List<S> loadAll() {
        return getScheduledJobDao().retrieveAll();
    }

    @Override
    public S loadById(int scheduledJobId) {
        return getScheduledJobDao().retrieveById(scheduledJobId);
    }

}
