/**
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com/>
 */
package akka.typed

import akka.event.Logging.{ LogEvent, LogLevel }

/**
 * An EventStream allows actors to register for certain message types, including
 * their subtypes automatically. Publishing events will broadcast them to all
 * currently subscribed actors with matching subscriptions for the event type.
 */
trait EventStream {
  /**
   * Attempts to register the subscriber to the specified Classifier
   * @return true if successful and false if not (because it was already
   *   subscribed to that Classifier, or otherwise)
   */
  def subscribe[T](subscriber: ActorRef[T], to: Class[T]): Boolean

  /**
   * Attempts to deregister the subscriber from the specified Classifier
   * @return true if successful and false if not (because it wasn't subscribed
   *   to that Classifier, or otherwise)
   */
  def unsubscribe[T](subscriber: ActorRef[T], from: Class[T]): Boolean

  /**
   * Attempts to deregister the subscriber from all Classifiers it may be subscribed to
   */
  def unsubscribe[T](subscriber: ActorRef[T]): Unit

  /**
   * Publishes the specified Event to this bus
   */
  def publish[T](event: T): Unit

  /**
   * Query the current minimum log level.
   */
  def logLevel: LogLevel

  /**
   * Change the current minimum log level.
   */
  def setLogLevel(loglevel: LogLevel): Unit
}

abstract class Logger {
  def initialBehavior: Behavior[Logger.Command]
}

object Logger {
  sealed trait Command
  case class Initialize(eventStream: EventStream, replyTo: ActorRef[ActorRef[LogEvent]]) extends Command
  // FIXME add Mute/Unmute (i.e. the TestEventListener functionality)
}