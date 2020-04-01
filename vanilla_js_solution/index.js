const courseURL = 'https://www.udemy.com/course/learn-flutter-dart-to-build-ios-android-apps/';
const proxyURL = 'https://cors-anywhere.herokuapp.com/';

function Video(name, length) {
	this.videoTitle = name;
	this.videoLength = length;
	this.lengthInSeconds = (function(length) {
		let timeSplit = length.split(':');

		if (timeSplit.length === 3) {
			return (
				parseInt(timeSplit[0]) * 60 * 60 +
				parseInt(timeSplit[1]) * 60 +
				parseInt(timeSplit[2])
			);
		}
		else {
			return parseInt(timeSplit[0]) * 60 + parseInt(timeSplit[1]);
		}
	})(this.videoLength);
}

fetch(proxyURL + courseURL)
	.then((res) => res.text())
	.then((res) => {
		getFreeVideos(res);
	})
	.catch((rej) => {
		console.log(`Error: ${rej}`);
	});

//parse udemy page
function getFreeVideos(html) {
	//create DOM from response html
	const coursePage = document.createElement('html');
	coursePage.innerHTML = html;

	const root = document.querySelector('#root ul');

	//selecting section with all videos from the course
	const curriculum = coursePage.querySelector('.ud-component--clp--curriculum');

	const resultList = [];

	//selecting all free lections and getting their Title and Length
	const previewVideosList = curriculum.querySelectorAll('.lecture-container--preview');
	previewVideosList.forEach((el) => {
		let videoTitle = el.querySelector('.title').textContent.trim();
		let videoLength = el.querySelector('.details .content-summary').textContent.trim();

		let video = new Video(videoTitle, videoLength);

		resultList.push(video);
	});

	//sort videos from shortest to longest
	resultList.sort((a, b) => {
		if (a.lengthInSeconds > b.lengthInSeconds) {
			return 1;
		}
		else if (a.lengthInSeconds < b.lengthInSeconds) {
			return -1;
		}
		else {
			return 0;
		}
	});

	//Generate sorted list of videos on HTML page
	resultList.forEach((el) => {
		let li = document.createElement('li');
		li.textContent = `${el.videoTitle} - ${el.videoLength}`;
		root.appendChild(li);

		// Print sorted list of videos in console
		// let video = `${el.videoTitle} - ${el.videoLength}`;
		// console.log(video);
	});
}
