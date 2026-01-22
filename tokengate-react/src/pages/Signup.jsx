import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api";

function Signup() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await API.post("/register", { username, email, password });
      alert("Verification email sent! Please check your inbox.");
      navigate("/login");
    } catch (err) {
      alert(err.response?.data || "Error occurred!");
    }
  };

  return (
    <div
    style={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      height: "100vh",        
      width: "100vw",           
      backgroundColor: "#0d0e0f", 
    }}
  >
    <div
      style={{
        width: "400px",             
        padding: "30px",          
        borderRadius: "10px",
        backgroundColor: "black",   
        boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
        textAlign: "center",
      }}
    >
      <h2>Signup</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          required
          onChange={(e) => setUsername(e.target.value)}
          style={{ width: "100%", padding: 8, marginBottom: 10 }}
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          required
          onChange={(e) => setEmail(e.target.value)}
          style={{ width: "100%", padding: 8, marginBottom: 10 }}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          required
          onChange={(e) => setPassword(e.target.value)}
          style={{ width: "100%", padding: 8, marginBottom: 10 }}
        />
        <button type="submit" style={{ padding: "8px 16px" }}>Signup</button>
      </form>
    </div>
  </div>
);
}

export default Signup;
