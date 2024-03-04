const jobService = require('../server/src/main/java/project/htjobboard/service/JobService');

exports.handler = async (event, context) => {
  try {
    const jobs = jobService.getAllJobs();

    return {
      statusCode: 200,
      body: JSON.stringify(jobs),
    };
  } catch (error) {
    console.error('Error fetching jobs:', error);

    return {
      statusCode: 500,
      body: JSON.stringify({ error: 'Internal Server Error' }),
    };
  }
};