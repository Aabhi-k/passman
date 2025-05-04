import { useState } from 'react';
import './Signup.css';
import { registerUser } from '../api/api';
import { deriveAESKey } from '../Encryption/CryptoUtils';
import { setAESKey } from '../Encryption/AesKeyStore';
import { useNavigate } from 'react-router-dom';
import passmanLogo from '../../assets/passmanlogo.png';

const Signup = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const [userData, setUserData] = useState({
    username: '',
    email: '',
    password: ''
  });

  const [loading, setLoading] = useState(false);

  let navigate = useNavigate();

  
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateForm = () => {
    if (!formData.username || !formData.email || !formData.password || !formData.confirmPassword) {
      setError('All fields are required');
      return false;
    }
    
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return false;
    }
    
    if (formData.password.length < 8) {
      setError('Password must be at least 8 characters long');
      return false;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      setError('Please enter a valid email address');
      return false;
    }
    
    return true;
  };




  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);
    
    if (!validateForm()) {
      return;
    }
    
    try {
      setUserData({
        username: formData.username,
        email: formData.email,
        password: formData.password
      });

      const response = await registerUser(userData);

      if (response.status !== 200) {
        setError('Signup failed. Please try again.');
        return;
      }
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('userId', response.data.userId);
      
      const salt = response.data.encryptedSalt;
      const aesKey = await deriveAESKey(formData.password, salt);
      setAESKey(aesKey);
            
      setSuccess('Account created successfully! Please check your email to verify your account.');
      
      navigate("/home");
      
    } catch (err) {
      setError('Signup failed. Please try again.');
      console.error('Signup error:', err);

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="signup-container">
      <div className="signup-form-box">
      <div className="logo-container">
          <img src={passmanLogo} alt="PassMan Logo" />
        </div>
        <h2 className="signup-subtitle">Create your account</h2>
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Choose a username"
              required
              className='form-input'
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Enter your email"
              required
              className='form-input'
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Create a password"
              required
              className='form-input'
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Confirm your password"
              required
              className='form-input'
            />
          </div>
          
          <button type="submit" className="signup-button">
            { loading ? (
              <div className="loading-spinner">
              <div className="spinner"></div>
              <span>Creating account...</span>
            </div>
            ):(
              'Create Account'
            )}
          </button>
        </form>
        
        <div className="signup-footer">
          <p className='signup-footer-text'>Already have an account? <a href="/login" className='signup-footer-link'>Log in</a></p>
        </div>
      </div>
    </div>
  );
};

export default Signup;