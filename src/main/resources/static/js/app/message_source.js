define([], function () {
  let messagepropertiesInner;
  if (typeof messageProperties !== 'undefined') {
    messagepropertiesInner = messageProperties;
  }

  function get(message, array) {
    if (array !== undefined) {
      return insertParams(messagepropertiesInner[message])
    } else {
      return messagepropertiesInner[message]
    }
  }

  function insertParams(message) {
    for (let i = 0; i < array.length; i++) {
      message.replace('{' + i + '}', array[i]);
    }
    return message;
  }

  return {
    get: get
  };
});