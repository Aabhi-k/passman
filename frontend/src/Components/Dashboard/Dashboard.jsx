import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Dashboard.css';
import { getAllPasswords, updatePassword } from '../api/api';
import { decryptData, encryptData } from '../Encryption/CryptoUtils';
import { getAESKey } from './../Encryption/AesKeyStore';


const Dashboard = () => {
  const [selectedPassword, setSelectedPassword] = useState(null);
  const [view, setView] = useState(false);
  const [passwords, setPasswords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  
  const [success, setSuccess] = useState('');
  const [showSuccessToast, setShowSuccessToast] = useState(false);
  const [isEditing, setIsEditing] = useState(false);

  const [refreshPasswords, setRefreshPasswords] = useState(true);
  const [editFormData, setEditFormData] = useState({
    title: '',
    username: '',
    password: '',
    url: '',
    description: '',
  });

  useEffect(() => {
    let toastTimer;
    if(showSuccessToast){
      toastTimer = setTimeout(() => {
        setShowSuccessToast(false);
      }, 3000);
    }
    return () => {
      if( toastTimer) clearTimeout(toastTimer);
    };
  }, [showSuccessToast]);
  const fetchPasswords = async () => {
    try {
      const response = await getAllPasswords(localStorage.getItem('userId'));
      const key = await getAESKey();
      if (response.status === 200 && response.data) {
        const decryptedPasswords = await Promise.all(
          response.data.map(async (encryptedPassword) => {
            try {
              return await decryptData(encryptedPassword, key);
            } catch (decryptError) {
              console.error('Failed to decrypt password:', decryptError);
              return {
                title: 'Decryption failed',
                username: 'Error',
                error: true
              };
            }
          })
        );
        
        setPasswords(decryptedPasswords);
      } else {
        setError('Failed to load your passwords. Please try again.');
      }
      setLoading(false);
    } catch (err) {
      console.error('Failed to fetch passwords:', err);
      setError('Failed to load your passwords. Please try again.');
      setLoading(false);
    }
  };


  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }   

    if(refreshPasswords){
      fetchPasswords();
      setRefreshPasswords(false);
    }

  }, [navigate, refreshPasswords]);

  const handleAddPassword = () => {
    navigate('/insert-password');
  };

  const handlePasswordClick = (password) => {
    setSelectedPassword(password);
    setView(true);

    // navigate(`/password/${passwordId}`);
  };
  const CloseModel = () =>{
    setView(false)
    setSelectedPassword(null);
    setIsEditing(false);

    setRefreshPasswords(true);

  }

  const EditPassword = () => {
    setEditFormData({
      title: selectedPassword.title,
      username: selectedPassword.username || '',
      password: selectedPassword.password || '',
      url: selectedPassword.url || '',
      description: selectedPassword.description || '',
    });
    
    setIsEditing(true);    
  }

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditFormData(prevData => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSave = async () => {
    const key = await getAESKey();
    const encryptedD = await encryptData(editFormData, key);
    const passwordDto = {...encryptedD, userId:localStorage.getItem('userId'), id: selectedPassword.id}

    try{
      const response = await updatePassword(passwordDto);

      if(response === 200){
        setSuccess("Password Updated successfully!");
        setShowSuccessToast(true);
        setIsEditing(false);
      }
    }
    catch(e){
      console.log("error: ", e);
    }
   
  }

  const handleCancelEdit = () =>{
    setIsEditing(false);
  }

  const handleDelete = () => {
    console.log("deleted")

    setRefreshPasswords(true);
  }
  return (
    <div className="dashboard-container">
      {showSuccessToast && (
        <div className="success-toast">
          <div className="toast-content">
            <span className="toast-icon">✓</span>
            <span className="toast-message">{success}</span>
          </div>
        </div>
      )}

      <header className="dashboard-header">
        <h1>Password Manager</h1>
        <div className="dashboard-actions">
          <button 
            className="add-password-btn"
            onClick={handleAddPassword}
          >
            Add Password
          </button>
          <button className="user-menu-btn">
            <i className="fas fa-user-circle"></i>
          </button>
        </div>
      </header>

      <main className="dashboard-content">
        {loading ? (
          <div className="loading-spinner">Loading your passwords...</div>
        ) : error ? (
          <div className="error-message">{error}</div>
        ) : passwords.length === 0 ? (
          <div className="empty-state">
            <h2>No passwords saved yet</h2>
            <p>Click "Add Password" to store your first password securely.</p>
            <button 
              className="add-password-btn-large"
              onClick={handleAddPassword}
            >
              Add Your First Password
            </button>
          </div>
        ) : (
          <div className="password-list-container">
          <ul className="password-list">
            {passwords.map((password, index) => (
              <li 
                key={password.id || index} 
                className={`password-list-item ${password.error ? 'password-item-error' : ''}`}
                onClick={() => handlePasswordClick(password)}
              >
                <div className="password-item-content">
                  <div className="password-title">{password.title}</div>
                  <div className="password-username">{password.username || 'No username'}</div>
                </div>
                <div className="password-item-actions">
                  <button 
                    className="copy-password-btn"
                    onClick={(e) => {
                      e.stopPropagation();
                      navigator.clipboard.writeText(password.password);
                    }}
                    title="Copy password"
                  >
                    Copy
                  </button>
                </div>
              </li>
            ))}
          </ul>
        </div>


      )}
      </main>
      {view && selectedPassword && (
  <div className="password-modal-overlay" onClick={CloseModel}>
    <div className="password-modal" onClick={(e) => e.stopPropagation()}>
      <div className="modal-header">
        <h2>{isEditing ? 'Edit Password' : selectedPassword.title}</h2>
        <button className="modal-close-btn" onClick={CloseModel}>×</button>
      </div>
      
      <div className="modal-body">
        {isEditing ? (
          // EDIT MODE
          <form className="edit-password-form">
            <div className="modal-field">
              <label htmlFor="edit-title">Title:</label>
              <input
                type="text"
                id="edit-title"
                name="title"
                value={editFormData.title}
                onChange={handleEditChange}
                className="edit-input"
              />
            </div>
            
            <div className="modal-field">
              <label htmlFor="edit-username">Username:</label>
              <input
                type="text"
                id="edit-username"
                name="username"
                value={editFormData.username}
                onChange={handleEditChange}
                className="edit-input"
              />
            </div>
            
            <div className="modal-field">
              <label htmlFor="edit-password">Password:</label>
              <input
                type="text"
                id="edit-password"
                name="password"
                value={editFormData.password}
                onChange={handleEditChange}
                className="edit-input"
              />
            </div>
            
            <div className="modal-field">
              <label htmlFor="edit-url">Website URL:</label>
              <input
                type="text"
                id="edit-url"
                name="url"
                value={editFormData.url}
                onChange={handleEditChange}
                className="edit-input"
              />
            </div>
            
            <div className="modal-field">
              <label htmlFor="edit-description">Description:</label>
              <textarea
                id="edit-description"
                name="description"
                value={editFormData.description}
                onChange={handleEditChange}
                className="edit-textarea"
                rows="3"
              />
            </div>
          </form>
        ) : (
          // VIEW MODE (your existing code)
          <>
            {selectedPassword.url && (
              <div className="modal-field">
                <label>Website:</label>
                <div className="field-value">
                  <a href={selectedPassword.url} target="_blank" rel="noopener noreferrer">
                    {selectedPassword.url}
                  </a>
                </div>
              </div>
            )}
            
            <div className="modal-field">
              <label>Username:</label>
              <div className="field-value field-with-copy">
                <span>{selectedPassword.username || 'No username'}</span>
                <button 
                  className="copy-btn"
                  onClick={() => {
                    navigator.clipboard.writeText(selectedPassword.username || '');
                  }}
                >
                  Copy
                </button>
              </div>
            </div>
            
            <div className="modal-field">
              <label>Password:</label>
              <div className="field-value field-with-copy">
                <span className="password-dots">••••••••••</span>
                <button 
                  className="copy-btn"
                  onClick={() => {
                    navigator.clipboard.writeText(selectedPassword.password || '');
                  }}
                >
                  Copy
                </button>
                <button 
                  className="show-btn"
                  onClick={(e) => {
                    const element = e.target.parentNode.querySelector('.password-dots');
                    if (element.textContent === '••••••••••') {
                      element.textContent = selectedPassword.password || '';
                      e.target.textContent = 'Hide';
                    } else {
                      element.textContent = '••••••••••';
                      e.target.textContent = 'Show';
                    }
                  }}
                >
                  Show
                </button>
              </div>
            </div>
            
            {selectedPassword.description && (
              <div className="modal-field">
                <label>Description:</label>
                <div className="field-value description">
                  {selectedPassword.description}
                </div>
              </div>
            )}
            
            <div className="modal-field metadata">
              <label>Added:</label>
              <div className="field-value">
                {new Date(selectedPassword.createdAt).toLocaleDateString()}
              </div>
            </div>
          </>
        )}
      </div>
      
      <div className="modal-footer">
        {isEditing ? (
          // Edit mode footer with Save and Cancel buttons
          <>
            <button className="cancel-btn" onClick={handleCancelEdit}>Cancel</button>
            <button className="save-btn" onClick={handleSave}>Save Changes</button>
          </>
        ) : (
          // View mode footer with Edit and Delete buttons
          <>
            <button className="edit-btn" onClick={EditPassword}>Edit</button>
            <button className="delete-btn" onClick={handleDelete}>Delete</button>
          </>
        )}
      </div>
    </div>
  </div>
)}


    </div>
  );
};


export default Dashboard;