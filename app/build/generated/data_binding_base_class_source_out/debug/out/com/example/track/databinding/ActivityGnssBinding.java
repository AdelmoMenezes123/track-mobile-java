// Generated by view binder compiler. Do not edit!
package com.example.track.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.track.R;
import com.example.track.cGnssView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityGnssBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final cGnssView circuloGnss;

  @NonNull
  public final View containerGnss;

  @NonNull
  public final View containerGnss2;

  @NonNull
  public final TextView textBearingGnss;

  @NonNull
  public final TextView textLatitudeGnss;

  @NonNull
  public final TextView textLongitudeGnss;

  @NonNull
  public final TextView textVelocidadeGnss;

  @NonNull
  public final TextView tituloBearingGnss;

  @NonNull
  public final TextView tituloLatitudeGnss;

  @NonNull
  public final TextView tituloLongitudeGnss;

  @NonNull
  public final TextView tituloVelocidadeGnss;

  private ActivityGnssBinding(@NonNull ConstraintLayout rootView, @NonNull cGnssView circuloGnss,
      @NonNull View containerGnss, @NonNull View containerGnss2, @NonNull TextView textBearingGnss,
      @NonNull TextView textLatitudeGnss, @NonNull TextView textLongitudeGnss,
      @NonNull TextView textVelocidadeGnss, @NonNull TextView tituloBearingGnss,
      @NonNull TextView tituloLatitudeGnss, @NonNull TextView tituloLongitudeGnss,
      @NonNull TextView tituloVelocidadeGnss) {
    this.rootView = rootView;
    this.circuloGnss = circuloGnss;
    this.containerGnss = containerGnss;
    this.containerGnss2 = containerGnss2;
    this.textBearingGnss = textBearingGnss;
    this.textLatitudeGnss = textLatitudeGnss;
    this.textLongitudeGnss = textLongitudeGnss;
    this.textVelocidadeGnss = textVelocidadeGnss;
    this.tituloBearingGnss = tituloBearingGnss;
    this.tituloLatitudeGnss = tituloLatitudeGnss;
    this.tituloLongitudeGnss = tituloLongitudeGnss;
    this.tituloVelocidadeGnss = tituloVelocidadeGnss;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityGnssBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityGnssBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_gnss, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityGnssBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.circuloGnss;
      cGnssView circuloGnss = ViewBindings.findChildViewById(rootView, id);
      if (circuloGnss == null) {
        break missingId;
      }

      id = R.id.containerGnss;
      View containerGnss = ViewBindings.findChildViewById(rootView, id);
      if (containerGnss == null) {
        break missingId;
      }

      id = R.id.containerGnss2;
      View containerGnss2 = ViewBindings.findChildViewById(rootView, id);
      if (containerGnss2 == null) {
        break missingId;
      }

      id = R.id.text_BearingGnss;
      TextView textBearingGnss = ViewBindings.findChildViewById(rootView, id);
      if (textBearingGnss == null) {
        break missingId;
      }

      id = R.id.text_latitudeGnss;
      TextView textLatitudeGnss = ViewBindings.findChildViewById(rootView, id);
      if (textLatitudeGnss == null) {
        break missingId;
      }

      id = R.id.text_longitudeGnss;
      TextView textLongitudeGnss = ViewBindings.findChildViewById(rootView, id);
      if (textLongitudeGnss == null) {
        break missingId;
      }

      id = R.id.text_VelocidadeGnss;
      TextView textVelocidadeGnss = ViewBindings.findChildViewById(rootView, id);
      if (textVelocidadeGnss == null) {
        break missingId;
      }

      id = R.id.titulo_BearingGnss;
      TextView tituloBearingGnss = ViewBindings.findChildViewById(rootView, id);
      if (tituloBearingGnss == null) {
        break missingId;
      }

      id = R.id.titulo_latitudeGnss;
      TextView tituloLatitudeGnss = ViewBindings.findChildViewById(rootView, id);
      if (tituloLatitudeGnss == null) {
        break missingId;
      }

      id = R.id.titulo_longitudeGnss;
      TextView tituloLongitudeGnss = ViewBindings.findChildViewById(rootView, id);
      if (tituloLongitudeGnss == null) {
        break missingId;
      }

      id = R.id.titulo_velocidadeGnss;
      TextView tituloVelocidadeGnss = ViewBindings.findChildViewById(rootView, id);
      if (tituloVelocidadeGnss == null) {
        break missingId;
      }

      return new ActivityGnssBinding((ConstraintLayout) rootView, circuloGnss, containerGnss,
          containerGnss2, textBearingGnss, textLatitudeGnss, textLongitudeGnss, textVelocidadeGnss,
          tituloBearingGnss, tituloLatitudeGnss, tituloLongitudeGnss, tituloVelocidadeGnss);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}