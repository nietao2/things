var kafkaEvent = require('./kafkaEvent'),
	kafka = require('kafka-node'),
	HighLevelProducer = kafka.HighLevelProducer,
	client = new kafka.Client(),
	producer = new HighLevelProducer(client),
	Consumer = kafka.Consumer,
	consumer = new Consumer(
	    client,
	    [
	        { topic: 'test2' }
	    ],{
	    	autoCommit: false
	    }
	);


consumer.on('message', function (message) {
	console.log(message);
	try{
//		var value = message.value;
		var value = JSON.parse(message.value);
		console.log(value);
		console.log('message key: ' + value.key);
//		kafkaEvent.kafkaEmitter.emit(value.key, message);
		var payloads = [
	    	    { topic: 'test', messages: '{"key":"'+value.key+'","value":"'+value.value+'12345"}' }
	    	];
	//producer.on('ready', function () {
		producer.send(payloads, function (err, data) {
			if(err) console.log(err);
		    console.log(data);
		});
		
	} catch(e){
		console.log(e);
	}
});

