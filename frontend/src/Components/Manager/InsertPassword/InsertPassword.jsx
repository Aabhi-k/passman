import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { encryptData } from '../../../Components/Encryption/CryptoUtils';
import { getAESKey } from '../../../Components/Encryption/AesKeyStore';
import './InsertPassword.css';
import { savePassword } from '../../api/api';

const InsertPassword = () => {
  const [formData, setFormData] = useState({
    title: '',
    username: '',
    password: '',
    url: '',
    description: '',
  });

  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  const handleGeneratePassword = () => {
    // Generate a strong random password
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?';
    let password = '';
    for (let i = 0; i < 16; i++) {
      password += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    
    setFormData(prevData => ({
      ...prevData,
      password
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.title || !formData.username || !formData.password) {
      setError('Fields is required');
      return;
    }

    setLoading(true);
    setError('');
    
    try {
      const aesKey = await getAESKey();
      
      if (!aesKey) {
        throw new Error('Encryption key not found. Please log in again.');
      }
      const encryptedData = await encryptData(formData, aesKey);
      const ed = {...encryptedData, userId:localStorage.getItem('userId')};
      const response = await savePassword(ed);

      if (response.status !== 200) {

        throw new Error('Failed to save password. Please try again.');
      }
      navigate('/dashboard');
      
    } catch (err) {
      console.error('Error saving password:', err);
      setError('Failed to save password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="insert-password-container">
      <div className="insert-password-header">
        <h1>Add New Password</h1>
        <button 
          className="back-button"
          onClick={() => navigate('/dashboard')}
        >
          Back to Dashboard
        </button>
      </div>
      
      {error && <div className="error-message">{error}</div>}
      
      <form className="password-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title <span className="required">*</span></label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="e.g. Gmail, Facebook, Bank Account"
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="username">Username/Email</label>
          <input
            type="text"
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            placeholder="Your login username or email"
          />
        </div>
        
        <div className="form-group password-input-group">
          <label htmlFor="password">Password</label>
          <div className="password-field">
            <input
              type={showPassword ? "text" : "password"}
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Enter password"
            />
            <button 
              type="button" 
              className="toggle-password-btn"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? "Hide" : "Show"}
            </button>
            <button 
              type="button" 
              className="generate-password-btn"
              onClick={handleGeneratePassword}
            >
              Generate
            </button>
          </div>
        </div>
        
        <div className="form-group">
          <label htmlFor="url">Website URL</label>
          <input
            type="text"
            id="url"
            name="url"
            value={formData.url}
            onChange={handleChange}
            placeholder="https://example.com"
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Add notes or additional details"
            rows="3"
          />
        </div>
        
        <div className="form-actions">
          <button 
            type="button" 
            className="cancel-btn"
            onClick={() => navigate('/dashboard')}
          >
            Cancel
          </button>
          <button 
            type="submit" 
            className="save-btn"
            disabled={loading}
          >
            {loading ? 'Saving...' : 'Save Password'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default InsertPassword;