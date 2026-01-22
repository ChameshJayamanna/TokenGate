import React from "react";
import { Navigate } from "react-router-dom";

function Home() {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" />;
  }

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
      <h2>Welcome Home!</h2>
      <p>Your JWT Token:</p>
      <small style={{ wordBreak: "break-all" }}>{token}</small>
    </div>
    </div>
  );
}

export default Home;
