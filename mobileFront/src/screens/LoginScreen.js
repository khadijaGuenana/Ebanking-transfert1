import React, { useState } from 'react'
import Logo from '../components/Logo'
import Header from '../components/Header'
import Button from '../components/Button'
import TextInput from '../components/TextInput'
import {phoneValidator} from '../helpers/phoneValidator'
import { passwordValidator } from '../helpers/passwordValidator'
import { View } from 'react-native'
import { GlobalStyles } from '../styles/globalStyles'
import ActivityIndicator from '../styles/ActivityIndicator'
import FlashMessage,{ showMessage } from "react-native-flash-message";
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
export default function LoginScreen({ navigation }) {
  const [phone, setPhone] = useState({ value: '', error: '' })
  const [password, setPassword] = useState({ value: '', error: '' })
  const baseUrl = "http://192.168.43.39:8080/"
  const onLoginPressed = async() => {
    const phoneError = phoneValidator(phone.value)
    const passwordError = passwordValidator(password.value)
    if (phoneError || passwordError) {
      setPhone({ ...phone, error:phoneError })
      setPassword({ ...password, error: passwordError })
      return
    }
    let payload = {
      phone:phone.value ,
      pin:password.value
    };
    console.log(payload)
    payload = JSON.stringify(payload);
    try {
      let res = await axios({
        method: 'POST',
        url: baseUrl+"client/login",
        data:payload,
        headers: {
          'Content-Type': 'application/json',
      }
      });
      if (res.status === 200) {
        //afficher data of user connected
        console.log(res.data)
        await AsyncStorage.setItem('user',JSON.stringify(res.data));
        navigation.reset({
          index: 0,
          routes: [{ name: 'Dashboard' }],
        })
      }
    } catch (err) {
      //console.log("heyoooo" + res);
      console.log("ERROOOOOR : "+err);
      if(err.response.status==404){
        showMessage({
          message: "Erreur d'authentification",
          description: "Telephone ou mot de passe invalide.",
          type: "danger",
          icon:"danger"
        });
      }else 
        showMessage({
          message: "Erreur de server",
          description: "Réssayer plus tard",
          type: "danger",
          icon:"danger"
        });
    }
    /*const options = {
      headers: {
          'Content-Type': 'application/json',
      }
    };
    axios.post(baseUrl+"client/login", payload, options)
    .then((res) => {
      await AsyncStorage.setItem('user',JSON.stringify(res.data));
      navigation.reset({
        index: 0,
        routes: [{ name: 'Dashboard' }],
      });
    })
    .catch((err) => {
      showMessage({
        message: "Erreur d'authentification",
        description: "Telephone ou mot de passe invalide.",
        type: "danger",
        icon:"danger"
      });
    })*/
    
  
  }
  return (
      <View style={GlobalStyles.containerLogin}>
      <Logo />
      <Header>Se connecté.</Header>
      <TextInput
        label="Telephone"
        returnKeyType="next"
        value={phone.value}
        onChangeText={(text) => setPhone({ value: text, error: '' })}
        error={!!phone.error}
        errorText={phone.error}
        autoCapitalize="none"
      />
      <TextInput
        label="Mot de passe"
        returnKeyType="done"
        value={password.value}
        onChangeText={(text) => setPassword({ value: text, error: '' })}
        error={!!password.error}
        errorText={password.error}
        secureTextEntry
      />
      <Button mode="contained" onPress={onLoginPressed}>
        Se connecter
      </Button>
      <FlashMessage
        position="top"
        style={{ marginVertical : 10 }}
        duration={2000}
      />
    </View>
  )
}
