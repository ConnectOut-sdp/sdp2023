const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendEventReminders = functions.database.ref("/Events/{eventId}")
    .onCreate((snapshot, context) => {
      const eventData = snapshot.val();
      const eventId = context.params.eventId;

      // Get the time of the event and calculate the times for the reminders
      const eventTime = new Date(eventData.time);
      const oneDayBefore = new Date(eventTime.getTime() -
      (24 * 60 * 60 * 1000));
      const twoHoursBefore = new Date(eventTime.getTime() -
      (2 * 60 * 60 * 1000));

      // Calculate the delays until each reminder should be sent
      const delayUntilOneDayBefore = Math.max(0,
          oneDayBefore.getTime() - Date.now());
      const delayUntilTwoHoursBefore = Math.max(0,
          twoHoursBefore.getTime() - Date.now());
      const delayUntilEventTime = Math.max(0, eventTime.getTime() - Date.now());

      // Schedule the notifications
      setTimeout(() => {
        const message = {
          notification: {
            title: `Upcoming event: ${eventData.title}`,
            body: `Event "${eventData.title}" is happening now`,
          },
          topic: "event_" + eventId,
        };

        admin.messaging().send(message)
            .then((response) => {
              console.log("Successfully sent event reminder:", response);
            })
            .catch((error) => {
              console.log("Error sending event reminder:", error);
            });
      }, delayUntilEventTime);

      setTimeout(() => {
        const message = {
          notification: {
            title: `Upcoming event: ${eventData.title}`,
            body: `Event "${eventData.title}" is happening in 2 hours`,
          },
          topic: "event_" + eventId,
        };

        admin.messaging().send(message)
            .then((response) => {
              console.log("Successfully sent 2-hour reminder:", response);
            })
            .catch((error) => {
              console.log("Error sending 2-hour reminder:", error);
            });
      }, delayUntilTwoHoursBefore);

      setTimeout(() => {
        const message = {
          notification: {
            title: `Upcoming event: ${eventData.title}`,
            body: `Event "${eventData.title}" is happening tomorrow`,
          },
          topic: "event_" + eventId,
        };

        admin.messaging().send(message)
            .then((response) => {
              console.log("Successfully sent 1-day reminder:", response);
            })
            .catch((error) => {
              console.log("Error sending 1-day reminder:", error);
            });
      }, delayUntilOneDayBefore);
    });
