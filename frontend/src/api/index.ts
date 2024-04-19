import axios from 'axios'

export const API_URL = `https://jsonplaceholder.typicode.com/` // DUMMY JSON

const $api = axios.create({
	baseURL: API_URL,
	headers: {
		'Content-Type': 'application/json',
	},
})


$api.interceptors.request.use((config) => {
	let token = localStorage.getItem('token');
	if (token !== null) {
		config.headers.Authorization = `Bearer ${token}`;
	}
	return config;
});

$api.interceptors.response.use((config) => {
	if (config.status === 401 && config.data?.message?.startsWith("JWT")) {
		localStorage.removeItem('token')
	}
	return config;
});

export default $api
