import { useState } from 'react';
import './Login.css';
import { loginUser } from '../api/api';
import { deriveAESKey } from '../Encryption/CryptoUtils';
import { getAESKey, setAESKey } from '../Encryption/AesKeyStore';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [credentials, setCredentials] = useState({
    email: '',
    password: ''
  });
  
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials({
      ...credentials,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    try {
     
      if (credentials.email && credentials.password) {
        const response = await loginUser(credentials);
        if (response.status === 200) {
          const data = response.data;
          console.log('Login successful:', data);
          localStorage.setItem('token', data.token);
          localStorage.setItem('userId', data.userId);
          const encryptedSalt = data.encryptionSalt;
          const aesKey = await deriveAESKey(credentials.password, encryptedSalt);
          setAESKey(aesKey);
          console.log("aeskey: ", getAESKey(), "+", aesKey);
          
          navigate('/dashboard');         
        } else {
          setError('Invalid email or password');
        }
      } else if (!credentials.email && !credentials.password) {
        setError('Please fill in all fields');
      } else {
        setError('Please fill in all fields');
      }
    } catch (e) {
      setError('Login failed. Please check your credentials.');
      console.error('Login error:', e);
    }finally {
      setLoading(false);
    }

  };

  return (
    <div className="login-container">
      <div className="login-form-box">
        <h1>PassMan</h1>
        <h2>Login to your account</h2>
        
        {error && <div className="error-message">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={credentials.email}
              onChange={handleChange}
              placeholder="Enter your email"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              placeholder="Enter your password"
              required
            />
          </div>
          
          <button type="submit" className="login-button">
            {loading ? (
             <div className="loading-spinner">
             <div className="spinner"></div>
             <span>Logging in...</span>
           </div>
            ) : 'Login'}
          </button>
        </form>
        
        <div className="login-footer">
          <p>Don't have an account? <a href="/signup">Sign up</a></p>
          <p><a href="/forgot-password">Forgot password?</a></p>
        </div>
      </div>
    </div>
  );
};

export default Login;