import React, { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import API from "../api";

function ResetPassword() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    try {
      await API.post("/reset-password", {
        token,
        newPassword: password,
      });

      alert("Password reset successful!");
      navigate("/login");
    } catch (err) {
      alert(err.response?.data || "Error resetting password!");
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
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          textAlign: "center",
        }}
      >
        <h2>Reset Password</h2>

        <form onSubmit={handleSubmit}>
          <input
            type="password"
            placeholder="New Password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{ width: "100%", padding: 8, marginBottom: 10 }}
          />

          <input
            type="password"
            placeholder="Confirm Password"
            required
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            style={{ width: "100%", padding: 8, marginBottom: 10 }}
          />

          <button type="submit" style={{ padding: "8px 16px" }}>
            Reset Password
          </button>
        </form>
      </div>
    </div>
  );
}

export default ResetPassword;