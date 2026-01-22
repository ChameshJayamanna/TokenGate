import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import API from "../api";

function VerifyEmail() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [message, setMessage] = useState("Verifying...");

  useEffect(() => {
    const token = searchParams.get("token");
    if (token) {
      API.get(`/verify?token=${token}`)
        .then(res => {
          setMessage(res.data);
          setTimeout(() => navigate("/login"), 2000);
        })
        .catch(err => {
          setMessage(err.response?.data || "Invalid or expired token!");
        });
    }
  }, [searchParams, navigate]);

  return (
    <div style={{ textAlign: "center", marginTop: 50 }}>
      <h3>{message}</h3>
    </div>
  );
}

export default VerifyEmail;
