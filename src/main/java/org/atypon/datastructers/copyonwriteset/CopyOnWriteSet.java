package org.atypon.datastructers.copyonwriteset;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CopyOnWriteSet<E> implements Set<E> {
    private final AtomicReference<Set<E>> ref;

    public CopyOnWriteSet( Collection<? extends E> c ) {
        ref = new AtomicReference<Set<E>>( new HashSet<E>( c ) );
    }

    @Override
    public boolean contains( Object o ) {
        return ref.get().contains( o );
    }

    @Override
    public boolean add( E e ) {
        while ( true ) {
            Set<E> current = ref.get();
            if ( current.contains( e ) ) {
                return false;
            }
            Set<E> modified = new HashSet<E>( current );
            modified.add( e );
            if ( ref.compareAndSet( current, modified ) ) {
                return true;
            }
        }
    }

    @Override
    public boolean remove( Object o ) {
        while ( true ) {
            Set<E> current = ref.get();
            if ( !current.contains( o ) ) {
                return false;
            }
            Set<E> modified = new HashSet<E>( current );
            modified.remove( o );
            if ( ref.compareAndSet( current, modified ) ) {
                return true;
            }
        }
    }





}
