import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { ChakraProvider } from "@chakra-ui/react";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import AuthenticatedContextProvider from "./Context/AuthContext";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <BrowserRouter>
    <ChakraProvider>
      <AuthenticatedContextProvider>
        <App />
      </AuthenticatedContextProvider>
    </ChakraProvider>
  </BrowserRouter>
);

reportWebVitals();
