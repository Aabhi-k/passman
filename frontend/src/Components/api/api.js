import axios from 'axios';
const apiURL = import.meta.env.VITE_API_URL;
const api = axios.create({
  baseURL:  apiURL,
  headers: {
    'Content-Type': 'application/json',
  },
});


export const registerUser = async (userData) => {
  try{
    localStorage.clear('token');
    const response = await api.post('/auth/register', userData, {
      withCredentials: true,
    });
    return response;
  }
  catch (error) {
    console.error('Error registering user:', error);
    throw error;
  }
}

export const loginUser = async (credentials) => {
  try {
    localStorage.clear('token');
    const response = await api.post('/auth/login', credentials, {
      withCredentials: true,
    });
    return response;
  }
  catch (error) {
    console.error('Error logging in user:', error);
    throw error;
  }
}

export const savePassword = async (passwordData) => {
  try {
    const response = await api.post('/passwords/insert', passwordData, {
      withCredentials: true,
    });
    return response;
  }
  catch (error) {
    console.error('Error inserting password:', error);
    throw error;
  }
}

export const getAllPasswords = async (userId) => {
  try {
    const response = await api.get(`/passwords/getall/${userId}`, {
      withCredentials: true,
    });
    return response;
  }
  catch (error) {
    console.error('Error fetching passwords:', error);
    throw error;
  }
}

export const getPasswordDetails = async (passwordId, userId) => {
  try {
    const response = await api.get(`/passwords/${userId}/${passwordId}`, {
      withCredentials: true,
    });
    return response;
  }
  catch (error) {
    console.error('Error fetching password details:', error);
    throw error;
  }

}


export const updatePassword = async (passwordData) => {
  try {
    const response = await api.put(`/passwords/update/${passwordData.id}`, passwordData, {
      withCredentials: true,
    });
    return response;
  }
  catch (error) {
    console.error('Error updating password:', error);
    throw error;
  }
}


api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);



export default api;