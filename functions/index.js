const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendEventReminders = functions.database.ref("/Events/{eventId}")
    .onCreate((snapshot, context) => {
      const eventData = snapshot.val();
      const eventId = context.params.eventId;

      // Get the time of the event and calculate the times for the reminders
      const eventTime = new Date(eventData.date);
      const oneDayBefore = new Date(eventTime.getTime() -
      (24 * 60 * 60 * 1000));
      const twoHoursBefore = new Date(eventTime.getTime() -
      (2 * 60 * 60 * 1000));

      // Schedule the notifications
      const oneDayBeforeJob = admin.pubsub.schedule(new Date(oneDayBefore))
          .topic(`event_${eventId}`)
          .message({
            notification: {
              title: `Upcoming event: ${eventData.title}`,
              body: `Event "${eventData.title}" is happening tomorrow`,
            },
          })
          .schedule();

      const twoHoursBeforeJob = admin.pubsub.schedule(new Date(twoHoursBefore))
          .topic(`event_${eventId}`)
          .message({
            notification: {
              title: `Upcoming event: ${eventData.title}`,
              body: `Event "${eventData.title}" is happening in 2 hours`,
            },
          })
          .schedule();

      const eventTimeJob = admin.pubsub.schedule(new Date(eventTime))
          .topic(`event_${eventId}`)
          .message({
            notification: {
              title: `Upcoming event: ${eventData.title}`,
              body: `Event "${eventData.title}" is happening now`,
            },
          })
          .schedule();

      console.log(`Scheduled event reminders for event_${eventId}:`);
      console.log(`1 day before: ${oneDayBeforeJob}`);
      console.log(`2 hours before: ${twoHoursBeforeJob}`);
      console.log(`Event time: ${eventTimeJob}`);
    });
