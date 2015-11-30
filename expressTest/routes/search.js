/**
 * http://usejsdoc.org/
 */
var kafkaEvent = require('../events/kafkaEvent');
var kafka = require('kafka-node'),
	HighLevelProducer = kafka.HighLevelProducer,
	client = new kafka.Client(),
	producer = new HighLevelProducer(client),
	Consumer = kafka.Consumer,
	consumer = new Consumer(
	    client,
	    [
	        { topic: 'test' }
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
		kafkaEvent.kafkaEmitter.emit(value.key, value);
		
	} catch(e){
		console.log(e);
	}
});

exports.search2 = function(req, res){
//	res.render('search', {search: req.query.q, results: "sdfsdfsdf"});
	
	/*setTimeout(function(req, res){
		var a = {};
		a.id = 1;
		a.text = req.query.q;
		res.json(a);
	},5000);*/
	
	var eventKey = 'kafka-back_' + req.ip;
	console.log(eventKey);
	var payloads = [
	    	    { topic: 'test2', messages: '{"key":"'+eventKey+'","value":"'+req.query.q+'"}' }
	    	];
	//producer.on('ready', function () {
		producer.send(payloads, function (err, data) {
			if(err) console.log(err);
		    console.log(data);
		});
	//});
	
	kafkaEvent.kafkaEmitter.once(eventKey,function(message){
		console.log('start renderring page!!!');
		res.send(message);
	});
	
};